package net.servokio.apo.App;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.util.Duration;
import net.servokio.apo.main;

public class Animations{
    public static final Duration DURATION = Duration.seconds(0.5);
    public void load_full_screen(){
        DoubleProperty stageX = new SimpleDoubleProperty();
        stageX.addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue.doubleValue() != Double.NaN) main.st.setX(newValue.doubleValue());
        });

        final Timeline slideLeft = new Timeline(
                new KeyFrame(DURATION, new KeyValue(stageX, 12,Interpolator.EASE_BOTH)),
                new KeyFrame(DURATION.multiply(2))
        );

        slideLeft.setOnFinished(event -> {
            slideLeft.jumpTo(Duration.ZERO);
            main.st.centerOnScreen();
            stageX.setValue(main.st.getX());
        });
    }
}
