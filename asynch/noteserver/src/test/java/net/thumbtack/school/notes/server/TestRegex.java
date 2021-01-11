package net.thumbtack.school.notes.server;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class TestRegex {

    @Test
    public void javaRegex(){
        String sectionName = "312312dadasdasd dsadawe aw  e eaweвфвфвфв";
        String regex = "[A-Za-zА-Яа-я0-9- ]+";
        Assert.assertTrue(sectionName.matches(regex));

        sectionName = "312312dadasdasd dsadawe ;№;№aw  e eaweвфвфвфв";
        Assert.assertFalse(sectionName.matches(regex));
    }
}
