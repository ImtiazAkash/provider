package com.service.provider.controller;

import com.service.provider.model.Profile;
import com.service.provider.service.ProfileService;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = "*")
public class ProfileController {

    @Autowired
    private ProfileService service;

    private static final String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads";

    @PostMapping("/saveprofile")
    public ResponseEntity<String> saveProfile(@RequestParam("providerName") String providerName,
            @RequestParam("bio") String bio, @RequestParam("email") String email,
            @RequestParam("phone") String phone, @RequestParam("city") String city,
            @RequestParam("country") String country, @RequestParam("image") MultipartFile image) throws IOException {

        File uploadDir = new File(UPLOAD_DIRECTORY);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // Save the image to the directory
        String imageFileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
        Path imagePath = Paths.get(UPLOAD_DIRECTORY, imageFileName);
        Files.write(imagePath, image.getBytes());

        Profile profile = new Profile();
        profile.setProviderName(providerName);
        profile.setBio(bio);
        profile.setEmail(email);
        profile.setPhone(phone);
        profile.setCity(city);
        profile.setCountry(country);
        profile.setImageFilePath(imagePath.toString());

        this.service.saveProfile(profile);

        return new ResponseEntity<>("Profile saved successfully", HttpStatus.OK);

    }

    @PutMapping("/updateprofile/{id}")
    public ResponseEntity<String> updateprofile(@PathVariable("id") long id, @RequestParam("providerName") String providerName,
            @RequestParam("bio") String bio, @RequestParam("email") String email,
            @RequestParam("phone") String phone, @RequestParam("city") String city,
            @RequestParam("country") String country, @RequestParam("image") MultipartFile image) throws IOException {

        Optional<Profile> optionalProfile = this.service.findProfileById(id);
        if (optionalProfile.isPresent()) {
            Profile profile = optionalProfile.get();

            // 2. Delete the old image file from the server (if it exists)
            String oldImagePath = profile.getImageFilePath();
            if (oldImagePath != null) {
                File oldImageFile = new File(oldImagePath);
                if (oldImageFile.exists()) {
                    oldImageFile.delete();  // Delete the old image file
                }
            }

            // 3. Save the new image to the server
            String newImageFileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
            Path newImagePath = Paths.get(UPLOAD_DIRECTORY, newImageFileName);

            // Ensure the directory exists (create if not)
            File uploadDir = new File(UPLOAD_DIRECTORY);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // Save the new image to the uploads directory
            Files.write(newImagePath, image.getBytes());

            profile.setProviderName(providerName);
            profile.setBio(bio);
            profile.setEmail(email);
            profile.setPhone(phone);
            profile.setCity(city);
            profile.setCountry(country);
            profile.setImageFilePath(newImagePath.toString());

            this.service.saveProfile(profile);

            return new ResponseEntity<>("Profile updated successfully", HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>("Profile update failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    

}