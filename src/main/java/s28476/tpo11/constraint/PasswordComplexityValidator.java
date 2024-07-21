package s28476.tpo11.constraint;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class PasswordComplexityValidator implements ConstraintValidator<PasswordComplexity, String> {

    private static final String LOWERCASE_PATTERN = ".*[a-z].*";
    private static final String UPPERCASE_PATTERN = ".*[A-Z].*[A-Z].*";
    private static final String DIGIT_PATTERN = ".*\\d.*\\d.*\\d.*";
    private static final String SPECIAL_CHARACTER_PATTERN = ".*[!@#$%^&*(),.?\":{}|<>].*[!@#$%^&*(),.?\":{}|<>].*[!@#$%^&*(),.?\":{}|<>].*[!@#$%^&*(),.?\":{}|<>].*";
    private static final String LENGTH_PATTERN = ".{10,}";

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null || password.isEmpty()) {
            return true;
        }

        List<String> errorMessages = new ArrayList<>();
        ResourceBundle bundle = ResourceBundle.getBundle("messages");

        if (!Pattern.matches(LOWERCASE_PATTERN, password)) {
            errorMessages.add(bundle.getString("linkDTO.constraint.password.lowercase"));
        }

        if (!Pattern.matches(UPPERCASE_PATTERN, password)) {
            errorMessages.add(bundle.getString("linkDTO.constraint.password.uppercase"));
        }

        if (!Pattern.matches(DIGIT_PATTERN, password)) {
            errorMessages.add(bundle.getString("linkDTO.constraint.password.digits"));
        }

        if (!Pattern.matches(SPECIAL_CHARACTER_PATTERN, password)) {
            errorMessages.add(bundle.getString("linkDTO.constraint.password.special"));
        }

        if (!Pattern.matches(LENGTH_PATTERN, password)) {
            errorMessages.add(bundle.getString("linkDTO.constraint.password.length"));
        }

        if (!errorMessages.isEmpty()) {
            context.disableDefaultConstraintViolation();
            for (String errorMessage : errorMessages) {
                context.buildConstraintViolationWithTemplate(errorMessage).addConstraintViolation();
            }
            return false;
        }

        return true;
    }
}