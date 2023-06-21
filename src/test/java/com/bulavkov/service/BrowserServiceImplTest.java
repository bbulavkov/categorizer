package com.bulavkov.service;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BrowserServiceImplTest {

    private final BrowserService service = new BrowserServiceImpl();

    @Test
    public void shouldBrowseText() throws IOException {
        try (MockedStatic<Jsoup> mockStatic = Mockito.mockStatic(Jsoup.class)) {

            String url = "https://www.google.com/search";
            String expected = "Some text";

            Connection connection = mock(Connection.class);
            Document document = mock(Document.class);
            Element element = mock(Element.class);

            when(element.text()).thenReturn(expected);
            when(document.body()).thenReturn(element);
            when(connection.get()).thenReturn(document);

            mockStatic.when(() -> Jsoup.connect(url))
                    .thenReturn(connection);

            String actual = service.browse(url);

            assertEquals(expected, actual);
        }
    }

    @Test
    public void shouldThrowExceptionWhenJsoupThrowsAny() throws IOException {
        try (MockedStatic<Jsoup> mockStatic = Mockito.mockStatic(Jsoup.class)) {

            String url = "https://www.google.com/search";

            Connection connection = mock(Connection.class);
            when(connection.get()).thenThrow(new RuntimeException("Something went wrong"));

            mockStatic.when(() -> Jsoup.connect(url))
                    .thenReturn(connection);

            RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
                service.browse(url);
            });

            assertEquals("java.lang.RuntimeException: Something went wrong", thrown.getMessage());
        }
    }
}