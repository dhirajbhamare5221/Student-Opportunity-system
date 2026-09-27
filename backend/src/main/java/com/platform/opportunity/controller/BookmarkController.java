package com.platform.opportunity.controller;

import com.platform.opportunity.dto.BookmarkDTO;
import com.platform.opportunity.service.BookmarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookmarks")
public class BookmarkController {

    @Autowired
    private BookmarkService bookmarkService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<BookmarkDTO>> getBookmarks(@PathVariable Long userId) {
        return ResponseEntity.ok(bookmarkService.getStudentBookmarks(userId));
    }

    @PostMapping("/{userId}/{opportunityId}")
    public ResponseEntity<?> addBookmark(@PathVariable Long userId, @PathVariable Long opportunityId) {
        try {
            BookmarkDTO bookmark = bookmarkService.addBookmark(userId, opportunityId);
            return ResponseEntity.ok(bookmark);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @DeleteMapping("/{userId}/{opportunityId}")
    public ResponseEntity<?> removeBookmark(@PathVariable Long userId, @PathVariable Long opportunityId) {
        try {
            bookmarkService.removeBookmark(userId, opportunityId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Bookmark removed successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
