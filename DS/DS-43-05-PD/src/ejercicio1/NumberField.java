package ejercicio1;


public final class NumberField extends Field{

    public NumberField(Integer field) {
        super(field);
    }

    
    @Override
    public String toString() {  
        return "Numberfield: " + getField() + "\n";
    }
    
    
}
