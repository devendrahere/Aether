package com.AETHER.music.service.implementaion;

import com.AETHER.music.entity.TrackFile;
import com.AETHER.music.exception.ResourceNotFoundException;
import com.AETHER.music.repository.TrackFileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;

@Service
public class TrackStreamingService {

    private static final long CHUNK_SIZE = 1024 * 1024;

    @Value("${music.storage.base-path}")
    private Path basePath;

    private final TrackFileRepository repository;

    public TrackStreamingService(TrackFileRepository repository) {
        this.repository = repository;
    }

    public ResponseEntity<ResourceRegion> stream(
            Long trackId,
            String quality,
            String rangeHeader
    ) throws IOException {

        TrackFile trackFile = repository
                .findByTrackIdAndQuality(trackId, quality)
                .orElseThrow(() -> new ResourceNotFoundException("Audio not found"));

        Path resolvedPath = basePath
                .resolve(trackFile.getFilePath())
                .normalize();

        if (!resolvedPath.startsWith(basePath)) {
            throw new SecurityException("Invalid file path");
        }

        Resource resource = new FileSystemResource(resolvedPath);

        if (!resource.exists()) {
            throw new ResourceNotFoundException("File missing on disk");
        }

        long contentLength = resource.contentLength();

        HttpRange range = rangeHeader != null
                ? HttpRange.parseRanges(rangeHeader).get(0)
                : HttpRange.createByteRange(0, CHUNK_SIZE - 1);

        long start = range.getRangeStart(contentLength);
        long end = range.getRangeEnd(contentLength);

        ResourceRegion region = new ResourceRegion(
                resource,
                start,
                Math.min(CHUNK_SIZE, end - start + 1)
        );

        return ResponseEntity
                .status(HttpStatus.PARTIAL_CONTENT)
                .contentType(MediaType.parseMediaType(resolveCodec(trackFile)))
                .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                .body(region);
    }

    private String resolveCodec(TrackFile tf) {
        return switch (tf.getCodec().toUpperCase()) {
            case "MP3" -> "audio/mpeg";
            case "FLAC" -> "audio/flac";
            default -> MediaType.APPLICATION_OCTET_STREAM_VALUE;
        };
    }
}