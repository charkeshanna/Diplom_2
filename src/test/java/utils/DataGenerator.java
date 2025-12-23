package utils;
import com.github.javafaker.Faker;


public class DataGenerator {
    private static final Faker faker = new Faker();

    public static String generateUserEmail() {
        return faker.internet().emailAddress();
    }

    public static String generateUserPassword() {
        return faker.internet().password(8, 12);
    }

}