package id.my.agungdh.nat1_website_api.service;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import id.my.agungdh.nat1_website_api.entity.Category;
import id.my.agungdh.nat1_website_api.entity.Post;
import id.my.agungdh.nat1_website_api.entity.PostStatus;
import id.my.agungdh.nat1_website_api.entity.Tag;
import id.my.agungdh.nat1_website_api.repository.CategoryRepository;
import id.my.agungdh.nat1_website_api.repository.PostRepository;
import id.my.agungdh.nat1_website_api.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostGeneratorService {

    private final AiService aiService;
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final ObjectMapper objectMapper;

    private static final String SYSTEM_PROMPT = """
            You are a professional tech blogger. Generate engaging, informative blog posts about programming, \
            software development, DevOps, AI, cloud computing, or other technology topics. \
            Write in markdown format with proper headings, code blocks, and formatting. \
            Respond ONLY with a valid JSON object, no other text.
            """;

    private static final String USER_PROMPT_TEMPLATE = """
            Generate a new tech blog post. Return a JSON object with this exact structure:
            {
              "title": "Post title",
              "slug": "post-slug-from-title",
              "content": "Full markdown content with ## headings, ```code blocks```, etc.",
              "excerpt": "A short 1-2 sentence summary",
              "category": {"name": "Category Display Name", "slug": "category-slug"},
              "tags": [{"name": "Tag Display Name", "slug": "tag-slug"}, ...]
            }
            
            Rules:
            - Slug must be lowercase, use hyphens, no special characters
            - Content must be at least 3 paragraphs with code examples
            - Include 2-4 relevant tags
            
            IMPORTANT: Generate a post on a COMPLETELY DIFFERENT topic from these existing posts:
            %s
            """;

    @Transactional
    @Scheduled(fixedDelayString = "${app.scheduler.interval-ms:300000}")
    public void generatePost() {
        try {
            log.info("Generating new blog post...");

            List<String> existingTitles = postRepository.findAll()
                    .stream()
                    .map(Post::getTitle)
                    .toList();

            String avoid = existingTitles.isEmpty()
                    ? "(no existing posts, any topic is fine)"
                    : String.join("\n", existingTitles.stream().map(t -> "- " + t).toList());

            String userPrompt = String.format(USER_PROMPT_TEMPLATE, avoid);
            String json = aiService.generate(SYSTEM_PROMPT, userPrompt);

            String cleaned = extractJson(json);
            Map<String, Object> data = objectMapper.readValue(cleaned,
                    new TypeReference<Map<String, Object>>() {});

            Post post = createPost(data);
            log.info("Generated post: {} (slug: {})", post.getTitle(), post.getSlug());
        } catch (Exception e) {
            log.error("Failed to generate post", e);
        }
    }

    private String extractJson(String raw) {
        String trimmed = raw.trim();
        if (trimmed.startsWith("```")) {
            int start = trimmed.indexOf("\n") + 1;
            int end = trimmed.lastIndexOf("```");
            if (end > start) {
                trimmed = trimmed.substring(start, end).trim();
            }
        }
        return trimmed;
    }

    private String resolveSlug(String slug, String title) {
        if (slug != null && !slug.isBlank()) {
            return postRepository.findBySlug(slug).isPresent()
                    ? slug + "-" + System.currentTimeMillis() % 10000
                    : slug;
        }
        String generated = title.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .replaceAll("^-|-$", "");
        if (generated.isEmpty()) {
            generated = "post-" + System.currentTimeMillis() % 100000;
        }
        return postRepository.findBySlug(generated).isPresent()
                ? generated + "-" + System.currentTimeMillis() % 10000
                : generated;
    }

    @SuppressWarnings("unchecked")
    private Post createPost(Map<String, Object> data) {
        Post post = new Post();
        post.setTitle((String) data.get("title"));
        post.setSlug(resolveSlug((String) data.get("slug"), post.getTitle()));
        post.setContent((String) data.get("content"));
        post.setExcerpt((String) data.get("excerpt"));
        post.setStatus(PostStatus.PUBLISHED);
        post.setPublishedAt(Instant.now().toEpochMilli());

        Map<String, Object> catData = (Map<String, Object>) data.get("category");
        if (catData != null) {
            String catSlug = (String) catData.get("slug");
            Category category = categoryRepository.findBySlug(catSlug)
                    .orElseGet(() -> {
                        Category c = new Category();
                        c.setSlug(catSlug);
                        c.setName((String) catData.getOrDefault("name", catSlug));
                        return categoryRepository.save(c);
                    });
            post.setCategory(category);
        }

        List<Map<String, Object>> tagList = (List<Map<String, Object>>) data.get("tags");
        if (tagList != null && !tagList.isEmpty()) {
            Set<Tag> tags = new HashSet<>();
            for (Map<String, Object> tagData : tagList) {
                String tagSlug = (String) tagData.get("slug");
                Tag tag = tagRepository.findBySlug(tagSlug)
                        .orElseGet(() -> {
                            Tag t = new Tag();
                            t.setSlug(tagSlug);
                            t.setName((String) tagData.getOrDefault("name", tagSlug));
                            return tagRepository.save(t);
                        });
                tags.add(tag);
            }
            post.setTags(tags);
        }

        return postRepository.save(post);
    }
}
