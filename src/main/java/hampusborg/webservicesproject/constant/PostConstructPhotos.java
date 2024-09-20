package hampusborg.webservicesproject.constant;

import org.springframework.stereotype.Service;

@Service
public class PostConstructPhotos {

    public String fetchDefaultAdminPhoto() {
        return "https://randomuser.me/api/portraits/men/1.jpg";
    }


    public String fetchDefaultUserPhoto() {
        return "https://randomuser.me/api/portraits/women/1.jpg";
    }
}
