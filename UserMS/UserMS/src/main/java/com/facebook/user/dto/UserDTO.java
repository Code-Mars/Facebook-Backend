package com.facebook.user.dto;

import java.time.LocalDate;

import org.hibernate.validator.constraints.Length;

import com.facebook.user.entity.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

public class UserDTO {
    private Long id;
    @NotNull(message = "{user.username.absent}")
    @Pattern(regexp = "[A-Z][a-z]*[A-Z]?[a-z0-9]*", message = "{user.username.invalid}")
    private String username;
    @NotNull(message="{user.fullname.absent}")
    @Length(min = 3, max = 20, message="{user.fullname.invalid}")
    @Pattern(regexp = "[A-Z][a-z]+ [A-Z][a-z]*", message= "{user.fullname.invalid}")
    private String fullName;
    @NotNull(message="{user.email.absent}")
    @Email(message="{user.email.invalid}")
    private String email;
    @NotNull(message="{user.dob.absent}")
    @Past(message="{user.dob.invalid}")
    private LocalDate dob;
    @NotNull(message="{user.gender.absent}")
    private Gender gender;
 @NotNull(message="{user.password.absent}")
 @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$", message="{user.password.invalid}")
    private String password;
    private String profilePicture;
    private String bio;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public LocalDate getDob() {
        return dob;
    }
    public void setDob(LocalDate dob) {
        this.dob = dob;
    }
    public Gender getGender() {
        return gender;
    }
    public void setGender(Gender gender) {
        this.gender = gender;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getProfilePicture() {
        return profilePicture;
    }
    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
    public String getBio() {
        return bio;
    }
    public void setBio(String bio) {
        this.bio = bio;
    }
    public static UserDTO entityToDTO(User user) {
        UserDTO userDTO= new UserDTO();
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setFullName(user.getFullName());
        userDTO.setGender(user.getGender());
        userDTO.setDob(user.getDob());
        userDTO.setId(user.getId());
        userDTO.setBio(user.getBio());
        userDTO.setProfilePicture(user.getProfilePicture());
        return userDTO;
    }
    public static User getEntity(UserDTO userDTO) {
        User user= new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setFullName(userDTO.getFullName());
        user.setGender(userDTO.getGender());
        user.setDob(userDTO.getDob());
        user.setPassword(userDTO.getPassword());
        user.setId(userDTO.getId());
        user.setBio(userDTO.getBio());
        user.setProfilePicture(userDTO.getProfilePicture());
        return user;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((username == null) ? 0 : username.hashCode());
        result = prime * result + ((fullName == null) ? 0 : fullName.hashCode());
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        result = prime * result + ((dob == null) ? 0 : dob.hashCode());
        result = prime * result + ((gender == null) ? 0 : gender.hashCode());
        result = prime * result + ((password == null) ? 0 : password.hashCode());
        result = prime * result + ((profilePicture == null) ? 0 : profilePicture.hashCode());
        result = prime * result + ((bio == null) ? 0 : bio.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UserDTO other = (UserDTO) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        if (fullName == null) {
            if (other.fullName != null)
                return false;
        } else if (!fullName.equals(other.fullName))
            return false;
        if (email == null) {
            if (other.email != null)
                return false;
        } else if (!email.equals(other.email))
            return false;
        if (dob == null) {
            if (other.dob != null)
                return false;
        } else if (!dob.equals(other.dob))
            return false;
        if (gender != other.gender)
            return false;
        if (password == null) {
            if (other.password != null)
                return false;
        } else if (!password.equals(other.password))
            return false;
        if (profilePicture == null) {
            if (other.profilePicture != null)
                return false;
        } else if (!profilePicture.equals(other.profilePicture))
            return false;
        if (bio == null) {
            if (other.bio != null)
                return false;
        } else if (!bio.equals(other.bio))
            return false;
        return true;
    }
    
}
