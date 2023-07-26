package ru.job4j.cinema.controller;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * The base behavior is checked.
 */
class IndexControllerTest {

    @Test
    public void getGetIndexThenGetIndexPage() {
        var indexController = new IndexController();
        var view = indexController.getIndex();
        assertThat(view).isEqualTo("index");
    }
}