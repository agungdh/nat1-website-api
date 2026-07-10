package id.my.agungdh.nat1_website_api.service;

import id.my.agungdh.nat1_website_api.dto.CategoryDto;
import id.my.agungdh.nat1_website_api.dto.ChatMessage;
import id.my.agungdh.nat1_website_api.dto.TagDto;
import id.my.agungdh.nat1_website_api.entity.Post;
import id.my.agungdh.nat1_website_api.mapper.CategoryMapper;
import id.my.agungdh.nat1_website_api.mapper.TagMapper;
import id.my.agungdh.nat1_website_api.repository.CategoryRepository;
import id.my.agungdh.nat1_website_api.repository.PostRepository;
import id.my.agungdh.nat1_website_api.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final AiService aiService;
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final CategoryMapper categoryMapper;
    private final TagMapper tagMapper;

    private static final DateTimeFormatter DATE_FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneId.of("UTC"));

    private static final Pattern FULL_QUERY = Pattern.compile(
            "(?:lihat|baca|tampilkan|show|get|ambil|full|detail|selengkapnya)\\s+(?:post|artikel|tulisan|blog)?\\s*[:\"']?([a-z0-9-]+)", Pattern.CASE_INSENSITIVE);
    private static final Pattern SEARCH_QUERY = Pattern.compile(
            "(?:cari|search|find|temukan|cariin)\\s+(?:post|artikel|tulisan|blog)?\\s*(?:tentang|soal|mengenai|about)?\\s*[:\"']?(.+)", Pattern.CASE_INSENSITIVE);

    public String chat(List<ChatMessage> history) {
        String lastMsg = history.isEmpty() ? "" : history.getLast().content();

        List<Map<String, Object>> messages = new ArrayList<>();

        String systemPrompt = buildSystemPrompt(lastMsg, history);
        messages.add(Map.of("role", "system", "content", systemPrompt));

        for (ChatMessage m : history) {
            messages.add(Map.of("role", m.role(), "content", m.content()));
        }

        Map<String, Object> response = aiService.chat(messages, null);
        return (String) response.getOrDefault("content", "");
    }

    private String buildSystemPrompt(String lastMsg, List<ChatMessage> history) {
        var sb = new StringBuilder();
        sb.append("""
                You are a helpful blog assistant for a tech blog. Answer user questions about the blog content.
                Always respond in the same language as the user's question.
                Always respond in markdown format with proper headings, lists, bold, and links where appropriate.
                Keep responses concise and well-structured.
                
                """);

        var m = FULL_QUERY.matcher(lastMsg);
        if (m.find()) {
            String slug = m.group(1).trim().toLowerCase();
            Post post = postRepository.findBySlug(slug).orElse(null);
            if (post != null) {
                sb.append("## User requested this specific post:\n\n");
                sb.append(postToFullString(post)).append("\n\n");
            }
        }

        var s = SEARCH_QUERY.matcher(lastMsg);
        if (s.find()) {
            String query = s.group(1).trim();
            List<Post> results = postRepository.searchByContent(query);
            sb.append("## Search results for \"").append(query).append("\":\n\n");
            sb.append(postsToList(results)).append("\n\n");
        }

        List<Post> posts = postRepository.findAll();
        if (!posts.isEmpty()) {
            sb.append("## All blog posts:\n\n");
            sb.append(postsToList(posts)).append("\n");
        }

        List<CategoryDto> categories = categoryMapper.toDtoList(categoryRepository.findAll());
        if (!categories.isEmpty()) {
            sb.append("## Categories:\n");
            for (var c : categories) {
                sb.append("- ").append(c.name()).append(" (slug: `").append(c.slug()).append("`)\n");
            }
            sb.append("\n");
        }

        List<TagDto> tags = tagMapper.toDtoList(tagRepository.findAll());
        if (!tags.isEmpty()) {
            sb.append("## Tags:\n");
            for (var t : tags) {
                sb.append("- ").append(t.name()).append(" (slug: `").append(t.slug()).append("`)\n");
            }
            sb.append("\n");
        }

        sb.append("""
                ## Instructions:
                - If the user asks to see the full content of a post with a specific slug, you have the content above
                - If listing posts, use the data provided above
                - If listing categories or tags, use the data provided above
                - Never use tool calls or XML tags in your response
                """);

        return sb.toString();
    }

    private String postsToList(List<Post> posts) {
        var sb = new StringBuilder();
        for (var post : posts) {
            sb.append("- **").append(post.getTitle()).append("**");
            if (post.getCategory() != null) {
                sb.append(" `").append(post.getCategory().getName()).append("`");
            }
            sb.append(" | slug: `").append(post.getSlug()).append("`");
            if (post.getPublishedAt() != null) {
                sb.append(" | ").append(DATE_FMT.format(Instant.ofEpochMilli(post.getPublishedAt())));
            }
            sb.append("\n");
            if (post.getExcerpt() != null) {
                sb.append("  > ").append(post.getExcerpt()).append("\n");
            }
        }
        if (posts.isEmpty()) {
            sb.append("*No posts found.*\n");
        }
        return sb.toString();
    }

    private String postToFullString(Post post) {
        var sb = new StringBuilder();
        sb.append("## ").append(post.getTitle()).append("\n\n");
        if (post.getCategory() != null) {
            sb.append("**Category:** ").append(post.getCategory().getName()).append("\n");
        }
        if (post.getPublishedAt() != null) {
            sb.append("**Published:** ").append(DATE_FMT.format(Instant.ofEpochMilli(post.getPublishedAt()))).append("\n");
        }
        sb.append("**Slug:** `").append(post.getSlug()).append("`\n\n---\n\n");
        sb.append(post.getContent());
        return sb.toString();
    }
}
