package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.testing.QBarTestProperties;
import mho.qbar.testing.QBarTesting;
import org.jetbrains.annotations.NotNull;

import static mho.qbar.objects.Variable.of;
import static mho.qbar.testing.QBarTesting.propertiesCompareToHelper;
import static mho.qbar.testing.QBarTesting.propertiesEqualsHelper;
import static mho.qbar.testing.QBarTesting.propertiesHashCodeHelper;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.*;

public class VariableProperties extends QBarTestProperties {
    private static final @NotNull String VARIABLE_CHARS = charsToString(range('a', 'z'));

    public VariableProperties() {
        super("Variable");
    }

    @Override
    protected void testBothModes() {
        propertiesGetIndex();
        propertiesOf();
        propertiesEquals();
        propertiesHashCode();
        propertiesCompareTo();
        propertiesRead();
        propertiesFindIn();
        propertiesToString();
    }

    private void propertiesGetIndex() {
        initialize("getIndex()");
        for (Variable v : take(LIMIT, P.variables())) {
            int index = v.getIndex();
            assertTrue(v, index >= 0);
            inverse(Variable::getIndex, Variable::of, v);
        }
    }

    private void propertiesOf() {
        initialize("of(int)");
        for (int i : take(LIMIT, P.naturalIntegersGeometric())) {
            Variable v = of(i);
            v.validate();
            inverse(Variable::of, Variable::getIndex, i);
        }
    }

    private void propertiesEquals() {
        initialize("equals(Object)");
        propertiesEqualsHelper(LIMIT, P, QBarIterableProvider::variables);
    }

    private void propertiesHashCode() {
        initialize("hashCode()");
        propertiesHashCodeHelper(LIMIT, P, QBarIterableProvider::variables);
    }

    private void propertiesCompareTo() {
        initialize("compareTo(Variable)");
        propertiesCompareToHelper(LIMIT, P, QBarIterableProvider::variables);
    }

    private void propertiesRead() {
        initialize("read(String)");
        QBarTesting.propertiesReadHelper(
            LIMIT,
            P,
            VARIABLE_CHARS,
            P.variables(),
            Variable::read,
            Variable::validate,
            false
        );
    }

    private void propertiesFindIn() {
        initialize("findIn(String)");
        propertiesFindInHelper(
                LIMIT,
                P.getWheelsProvider(),
                P.variables(),
                Variable::read,
                Variable::findIn,
                Variable::validate
        );
    }

    private void propertiesToString() {
        initialize("toString()");
        propertiesToStringHelper(LIMIT, VARIABLE_CHARS, P.variables(), Variable::read);
    }
}
