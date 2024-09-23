package hampusborg.webservicesproject.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ApiUser {
    private List<User> results;

    @Getter
    @Setter
    public static class User {
        private Name name;
        private String email;
        private String phone;
        private Login login;
        private Location location;
        private Picture picture;

        @Getter
        @Setter
        public static class Name {
            private String first;
            private String last;
        }

        @Getter
        @Setter
        public static class Login {
            private String username;
            private String password;
        }

        @Getter
        @Setter
        public static class Location {
            private Street street;
            private String city;

            @Getter
            @Setter
            public static class Street {
                private String name;
            }
        }

        @Getter
        @Setter
        public static class Picture {
            private String large;
        }
    }
}