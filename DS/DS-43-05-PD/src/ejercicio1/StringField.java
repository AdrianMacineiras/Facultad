package ejercicio1;

public final class StringField extends Field { 

    public StringField(String field) {
        super(field);
    }
        
    @Override
    public String toString() {
        return "Stringfield: " + getField() + "\n";
    }
     
}
