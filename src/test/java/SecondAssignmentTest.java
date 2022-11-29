import org.example.SecondAssignment;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertThrows;

class SecondAssignmentTest {
    @Test
    void testExceptions(){
        assertThrows(FileNotFoundException.class, ()-> SecondAssignment.main(new String[]{"unexciting.xml"}));
        assertThrows(FileNotFoundException.class, ()-> SecondAssignment.main(new String[]{"unexciting.json"}));
        assertThrows(IllegalArgumentException.class, ()-> SecondAssignment.main(new String[]{}));
    }
}