import org.example.FirstAssignment;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertThrows;

class FirstAssignmentTest {
    @Test
    void testExceptions(){
        assertThrows(FileNotFoundException.class, ()-> FirstAssignment.main(new String[]{"unexciting.xml"}));
        assertThrows(IllegalArgumentException.class, ()-> FirstAssignment.main(new String[]{}));
    }
}
