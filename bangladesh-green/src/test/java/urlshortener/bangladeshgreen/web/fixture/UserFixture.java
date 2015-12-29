package urlshortener.bangladeshgreen.web.fixture;

import urlshortener.bangladeshgreen.domain.User;
import urlshortener.bangladeshgreen.secure.Hash;

/**
 * Created by ismaro3.
 */
public class UserFixture {

    public static User someUser() {
        return new User("user","user@mail.com","user", Hash.makeHash("password"),"Real name");
    }

    public static User someUser2() {
        return new User("user2","user2@mail.com","user", Hash.makeHash("password2"),"Real name2");
    }


}
