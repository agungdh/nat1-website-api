package id.my.agungdh.nat1_website_api.service;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import id.my.agungdh.nat1_website_api.dto.CategoryDto;
import id.my.agungdh.nat1_website_api.dto.PostDto;
import id.my.agungdh.nat1_website_api.dto.TagDto;
import id.my.agungdh.nat1_website_api.entity.Post;
import id.my.agungdh.nat1_website_api.mapper.CategoryMapper;
import id.my.agungdh.nat1_website_api.mapper.PostMapper;
import id.my.agungdh.nat1_website_api.mapper.TagMapper;
import id.my.agungdh.nat1_website_api.repository.CategoryRepository;
import id.my.agungdh.nat1_website_api.repository.PostRepository;
import id.my.agungdh.nat1_website_api.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final AiService aiService;
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final PostMapper postMapper;
    private final CategoryMapper categoryMapper;
    private final TagMapper tagMapper;
    private final ObjectMapper objectMapper;

    private static final String SYSTEM_PROMPT = """
            You are a helpful blog assistant. You can help users find blog posts, list categories and tags.
            Use the available tools to answer user questions about the blog content.
            Always respond in the same language as the user's question.
            When listing posts, include title, slug, excerpt, and published date.
            """;

    private static final List<Map<String, Object>> TOOLS = List.of(
            tool("search_posts", "Search blog posts by full-text search on content",
                    Map.of("query", Map.of("type", "string", "description", "Search query"))),
            tool("list_posts", "List all blog posts",
                    Map.of()),
            tool("get_post", "Get full content of a blog post by its slug",
                    Map.of("slug", Map.of("type", "string", "description", "Post slug"))),
            tool("list_categories", "List all blog categories",
                    Map.of()),
            tool("list_tags", "List all blog tags",
                    Map.of())
    );

    @SuppressWarnings("unchecked")
    public String chat(String userMessage) {
        List<Map<String, Object>> messages = new ArrayList<>();
        messages.add(Map.of("role", "system", "content", SYSTEM_PROMPT));
        messages.add(Map.of("role", "user", "content", userMessage));

        Map<String, Object> message = aiService.chat(messages, TOOLS);

        List<Map<String, Object>> toolCalls = (List<Map<String, Object>>) message.get("tool_calls");

        if (toolCalls != null && !toolCalls.isEmpty()) {
            messages.add(message);

            for (Map<String, Object> toolCall : toolCalls) {
                Map<String, Object> function = (Map<String, Object>) toolCall.get("function");
                String name = (String) function.get("name");
                String argumentsStr = (String) function.get("arguments");
                String callId = (String) toolCall.get("id");

                String result = executeTool(name, argumentsStr);

                messages.add(Map.of("role", "tool", "tool_call_id", callId, "content", result));
            }

            message = aiService.chat(messages, null);
        }

        return (String) message.getOrDefault("content", "");
    }

    private String executeTool(String name, String argumentsStr) {
        try {
            Map<String, Object> args = argumentsStr != null && !argumentsStr.isEmpty()
                    ? objectMapper.readValue(argumentsStr, new TypeReference<Map<String, Object>>() {})
                    : Map.of();

            return switch (name) {
                case "search_posts" -> {
                    String query = (String) args.getOrDefault("query", "");
                    List<Post> posts = postRepository.searchByContent(query);
                    yield postsToString(posts);
                }
                case "list_posts" -> {
                    List<Post> posts = postRepository.findAll();
                    yield postsToString(posts);
                }
                case "get_post" -> {
                    String slug = (String) args.get("slug");
                    Post post = postRepository.findBySlug(slug).orElse(null);
                    yield post != null ? postToFullString(post) : "Post not found";
                }
                case "list_categories" -> {
                    List<CategoryDto> categories = categoryMapper.toDtoList(categoryRepository.findAll());
                    yield objectMapper.writeValueAsString(categories);
                }
                case "list_tags" -> {
                    List<TagDto> tags = tagMapper.toDtoList(tagRepository.findAll());
                    yield objectMapper.writeValueAsString(tags);
                }
                default -> "Unknown tool: " + name;
            };
        } catch (Exception e) {
            return "Error executing tool: " + e.getMessage();
        }
    }

    private String postsToString(List<Post> posts) {
        var sb = new StringBuilder();
        for (int i = 0; i < posts.size(); i++) {
            Post post = posts.get(i);
            sb.append(i + 1).append(". **").append(post.getTitle()).append("**\n");
            sb.append("   Slug: ").append(post.getSlug()).append("\n");
            if (post.getExcerpt() != null) {
                sb.append("   Excerpt: ").append(post.getExcerpt()).append("\n");
            }
            if (post.getPublishedAt() != null) {
                sb.append("   Published: ").append(post.getPublishedAt()).append("\n");
            }
            if (post.getCategory() != null) {
                sb.append("   Category: ").append(post.getCategory().getName()).append("\n");
            }
            sb.append("\n");
        }
        if (posts.isEmpty()) {
            sb.append("No posts found.");
        }
        return sb.toString();
    }

    private String postToFullString(Post post) {
        var sb = new StringBuilder();
        sb.append("Title: ").append(post.getTitle()).append("\n");
        sb.append("Slug: ").append(post.getSlug()).append("\n");
        if (post.getCategory() != null) {
            sb.append("Category: ").append(post.getCategory().getName()).append("\n");
        }
        if (post.getPublishedAt() != null) {
            sb.append("Published: ").append(post.getPublishedAt()).append("\n");
        }
        sb.append("\n").append(post.getContent());
        return sb.toString();
    }

    private static Map<String, Object> tool(String name, String description, Map<String, Object> properties) {
        Map<String, Object> function = new HashMap<>();
        function.put("name", name);
        function.put("description", description);
        function.put("parameters", Map.of(
                "type", "object",
                "properties", properties,
                "required", properties.keySet()
        ));
        return Map.of("type", "function", "function", function);
    }
}
