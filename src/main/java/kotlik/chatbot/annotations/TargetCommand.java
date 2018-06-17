package kotlik.chatbot.annotations;

import kotlik.chatbot.parser.Command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TargetCommand {
    Command value() default Command.UNKNOWN;
}
