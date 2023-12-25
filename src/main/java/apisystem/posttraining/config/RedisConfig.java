//package apisystem.posttraining.config;
//
//import com.fasterxml.jackson.annotation.JsonTypeInfo;
//import com.fasterxml.jackson.core.JsonGenerator;
//import com.fasterxml.jackson.databind.DeserializationFeature;
//import com.fasterxml.jackson.databind.MapperFeature;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.fasterxml.jackson.databind.json.JsonMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.ResourceLoader;
//import org.springframework.data.redis.cache.RedisCacheConfiguration;
//import org.springframework.data.redis.cache.RedisCacheManager;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
//import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
//import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.*;
//
//import java.time.Duration;
//
//@Configuration
//public class RedisConfig {
//    @Value("${redis.host}")
//    private String redisHost;
//
//    @Value("${redis.port}")
//    private int redisPort;
//
//    @Bean
//    public LettuceConnectionFactory redisConnectionFactory() {
//        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(redisHost, redisPort);
////        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
////                .useSsl().build();
//        return new LettuceConnectionFactory(configuration);
//    }
//
////
////    @Bean
////    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
////        return RedisCacheManager.create(connectionFactory);
////    }
//
////    @Bean
////    public RedisCacheManager cacheManager() {
////        RedisCacheConfiguration cacheConfig = myDefaultCacheConfig(Duration.ofMinutes(10)).disableCachingNullValues();
////
////        return RedisCacheManager.builder(redisConnectionFactory())
////                .cacheDefaults(cacheConfig)
////                .withCacheConfiguration("tutorials", myDefaultCacheConfig(Duration.ofMinutes(5)))
////                .withCacheConfiguration("tutorial", myDefaultCacheConfig(Duration.ofMinutes(1)))
////                .build();
////    }
//
//    @Bean
//    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.enable(JsonGenerator.Feature.IGNORE_UNKNOWN);
//        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
//        objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
//
//        RedisSerializer<Object> serializer = new GenericJackson2JsonRedisSerializer(objectMapper);
//
//
//        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
//                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
//                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer))
//                .entryTtl(Duration.ofMinutes(1));
//
//        return RedisCacheManager.builder(redisConnectionFactory)
//                .cacheDefaults(config)
//                .build();
//    }
//
////    @Bean
////    public RedisCacheManager cacheManager(
////            RedisConnectionFactory redisConnectionFactory,
////            ResourceLoader resourceLoader,
////            ObjectMapper objectMapper) {
//////        RedisSerializer<Object> serializer = new GenericJackson2JsonRedisSerializer(objectMapper);
////        RedisSerializer<Object> serializer = new JdkSerializationRedisSerializer(resourceLoader.getClassLoader());
////
////        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
////                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
////                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer));
////
////        return RedisCacheManager.builder(redisConnectionFactory).cacheDefaults(config).build();
////    }
////
////    private RedisCacheConfiguration myDefaultCacheConfig(Duration duration) {
////        return RedisCacheConfiguration
////                .defaultCacheConfig()
////                .entryTtl(duration)
////                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper())));
////    }
//
//    @Bean
//    public RedisTemplate<Object, Object> sessionRedisTemplate(
//            RedisConnectionFactory connectionFactory) {
//        RedisTemplate<Object, Object> template = new RedisTemplate<Object, Object>();
//        template.setKeySerializer(new StringRedisSerializer());
//        template.setHashKeySerializer(new StringRedisSerializer());
//
//        template.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
//
//        template.setConnectionFactory(connectionFactory);
//        return template;
//    }
//
//    @Bean
//    public ObjectMapper objectMapper() {
//        return JsonMapper.builder()
//                .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS, true)
//                .configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false)
//                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
//                .configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true)
//                .addModule(new JavaTimeModule())
//                .findAndAddModules()
//                .build();
//    }
//
////    @Bean
////    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
////        var jacksonSerializer = new GenericJackson2JsonRedisSerializer(objectMapper());
////
////        var valueSerializer = RedisSerializationContext.SerializationPair.fromSerializer(jacksonSerializer);
////        return (builder) -> builder
////                .withCacheConfiguration(CacheNames.MAKER_QUIZ_CACHE,
////                        RedisCacheConfiguration.defaultCacheConfig()
////                                .serializeValuesWith(valueSerializer)
////                                .entryTtl(Duration.ofMinutes(200)))
////                .withCacheConfiguration(CacheNames.MAKER_SHORT_CACHE,
////                        RedisCacheConfiguration.defaultCacheConfig()
////                                .serializeValuesWith(valueSerializer)
////                                .entryTtl(Duration.ofMinutes(60)))
////                .withCacheConfiguration(CacheNames.MAKER_LONG_CACHE,
////                        RedisCacheConfiguration.defaultCacheConfig()
////                                .serializeValuesWith(valueSerializer)
////                                .entryTtl(Duration.ofMinutes(60 * 24)));
////
//////...
////    }
//}
