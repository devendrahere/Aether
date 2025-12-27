package com.AETHER.music.controller;


import com.AETHER.music.entity.TrackFile;
import com.AETHER.music.exception.ResourceNotFoundException;
import com.AETHER.music.repository.TrackFileRepository;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/tracks")
public class TrackStreamingController {
    private static final int DEFAULT_CHUNK_SIZE = 1024*1024;

    private final TrackFileRepository trackFileRepository;

    public TrackStreamingController(
            TrackFileRepository trackFileRepository
    ){
        this.trackFileRepository=trackFileRepository;
    }

    @GetMapping("/{trackId}/stream")
    public ResponseEntity<Resource> stream(
            @PathVariable Long trackId,
            @RequestParam String quality,
            @RequestHeader(value = "Range",required = false) String rangeHeader
    ) throws IOException{
        TrackFile trackFile=trackFileRepository.
                findByTrackIdAndQuality(trackId,quality)
                .orElseThrow(()->new ResourceNotFoundException("Audio not found!"));
        File file=new File(trackFile.getFilePath());
        long fileSize=file.length();

        long start=0;
        long end=fileSize-1;

        if(rangeHeader !=null && rangeHeader.startsWith("bytes=")){
            String[] ranges=rangeHeader.replace("bytes=","").split("-");
            start=Long.parseLong(ranges[0]);
            if(ranges.length>1 && !ranges[1].isEmpty()){
                end=Long.parseLong(ranges[1]);
            }else {
                end=Math.min(start+DEFAULT_CHUNK_SIZE-1,fileSize-1);
            }
        }
        long contentLength=end-start+1;

        InputStream inputStream=new FileInputStream(file);
        inputStream.skip(start);

        InputStreamResource resource=new InputStreamResource(inputStream);

        HttpHeaders headers=new HttpHeaders();

        headers.set(HttpHeaders.CONTENT_TYPE,resolveContentType(trackFile.getCodec()));
        headers.set(HttpHeaders.ACCEPT_RANGES,"bytes");
        headers.set(HttpHeaders.CONTENT_RANGE,
                "bytes "+start+"-"+end+"/"+fileSize);
        headers.setContentLength(contentLength);

        return new ResponseEntity<>(resource,headers, HttpStatus.PARTIAL_CONTENT);
    }


    private String resolveContentType(String  codec){
        return switch(codec.toUpperCase()){
            case "MP3" ->"audio/mpeg";
            case "FLAC"->"audio/flac";
            default -> MediaType.APPLICATION_OCTET_STREAM_VALUE;
        };
    }
}