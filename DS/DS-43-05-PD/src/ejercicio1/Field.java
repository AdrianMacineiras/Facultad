package ejercicio1;

import java.util.Objects;


public abstract class Field<E> {
    private final E field;

    public Field(E field) {
        this.field = field;
    }

    public E getField() {
        return field;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Field<?> other = (Field<?>) obj;
        return Objects.equals(this.field, other.field);
    }

   

    @Override
    public abstract String toString();

}
