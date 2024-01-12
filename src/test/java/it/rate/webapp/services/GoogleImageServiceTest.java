package it.rate.webapp.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import it.rate.webapp.BaseTest;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

class GoogleImageServiceTest extends BaseTest {

  @Autowired GoogleImageService googleImageService;
  @MockBean SecurityContext securityContext;
  @MockBean Authentication authentication;
  @MockBean Drive.Files files;

  @Test
  void savePlaceImageHappyCase() throws IOException {
    Long mockPlaceId = 1L;
    String mockDiskId = "id";
    MockMultipartFile imageMock =
        new MockMultipartFile("data", "image.jpeg", "image/jpeg", "picture.jpeg".getBytes());

    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("Joe");

    Drive.Files.Create create = mock(Drive.Files.Create.class, RETURNS_DEEP_STUBS);
    File file = mock(File.class, RETURNS_DEEP_STUBS);

    when(file.getId()).thenReturn(mockDiskId);
    when(create.execute()).thenReturn(file);

    when(drive.files()).thenReturn(files);
    when(files.create(any(), any())).thenReturn(create);

    String id = googleImageService.savePlaceImage(imageMock, mockPlaceId);
    assertEquals(mockDiskId, id);
  }
}
