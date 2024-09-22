package hampusborg.webservicesproject.service;

import org.springframework.stereotype.Service;

@Service
public class PostConstructPhotos {

    public static String fetchDefaultAdminPhoto() {
        return "https://randomuser.me/api/portraits/men/1.jpg";
    }
}