package gr1.fpt.bambikitchen.firebase.model.constraints;

import lombok.Getter;

@Getter

public enum Platform {
    ANDROID("Android"),
    WEB("Web"),
    IOS("IOS");

    private final String value;

    Platform(String value) {
        this.value = value;
    }

}
