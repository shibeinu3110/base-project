package com.shiba.baseproject.controller;

import com.shiba.baseproject.common.StandardResponse;
import com.shiba.baseproject.dto.request.SignUpRequest;
import com.shiba.baseproject.dto.response.TokenResponse;
import com.shiba.baseproject.elastic.BookESService;
import com.shiba.baseproject.elastic.BookRepositoryES;
import com.shiba.baseproject.elastic.BooksES;
import com.shiba.baseproject.security.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Slf4j(topic = "AUTHENTICATION-CONTROLLER")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AuthenticationController {
    AuthenticationService authenticationService;
    BookESService bookESService;
    BookRepositoryES bookRepositoryES;
    @PostMapping("/sign-up")
    public StandardResponse<String> signUp(@RequestBody SignUpRequest signUpRequest) {
        log.info("Sign up");
        authenticationService.signUp(signUpRequest);
        return StandardResponse.build("Sign up");
    }

    @PostMapping("/sign-in")
    public StandardResponse<TokenResponse> signIn(@RequestBody SignUpRequest signInRequest) {
        log.info("Sign in");
        TokenResponse tokenResponse = authenticationService.signIn(signInRequest);
        return StandardResponse.build(tokenResponse, "Sign in successfully");
    }

    @PostMapping("/books-index")
    public StandardResponse<String> booksIndex(@RequestBody List<BooksES> booksESList) {
        log.info("Books index");
        bookESService.saveAllBooks(booksESList);
        return StandardResponse.build("Books index");
    }

    @GetMapping("/get-all")
    public StandardResponse<List<BooksES>> getAll() {
        log.info("Books index");

        return StandardResponse.build(bookESService.getAllBooks(),"Books index");
    }

    @GetMapping("/get-terms")
    public StandardResponse<List<BooksES>> findTerms(@RequestParam String terms) {
        log.info("Books index");

        return StandardResponse.build(bookESService.searchBooksByTitle(terms),"Books index");
    }

    @DeleteMapping("/delete")
    public StandardResponse<String> delete() {
        log.info("Books index");
        bookRepositoryES.deleteAll();
        return StandardResponse.build("Books index");
    }



}
