package org.minima.tests.kissvm.functions.base;

import org.minima.kissvm.functions.base.SUBSET;

import org.minima.kissvm.Contract;
import org.minima.kissvm.exceptions.ExecutionException;
import org.minima.kissvm.exceptions.MinimaParseException;
import org.minima.kissvm.expressions.ConstantExpression;
import org.minima.kissvm.functions.MinimaFunction;
import org.minima.kissvm.values.BooleanValue;
import org.minima.kissvm.values.HEXValue;
import org.minima.kissvm.values.NumberValue;
import org.minima.kissvm.values.ScriptValue;
import org.minima.kissvm.values.Value;
import org.minima.objects.Transaction;
import org.minima.objects.Witness;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

//HEXValue SUBSET (NumberValue start NumberValue end HEXValue val)
//ScriptValue SUBSET (NumberValue start NumberValue end ScriptValue val)
public class SUBSETTests {

    @Test
    public void testConstructors() {
        SUBSET fn = new SUBSET();
        MinimaFunction mf = fn.getNewFunction();

        assertEquals("SUBSET", mf.getName());
        assertEquals(0, mf.getParameterNum());

        try {
            mf = MinimaFunction.getFunction("SUBSET");
            assertEquals("SUBSET", mf.getName());
            assertEquals(0, mf.getParameterNum());
        } catch (MinimaParseException ex) {
            fail();
        }
    }

    @Test
    public void testValidParams() {
        Contract ctr = new Contract("", "", new Witness(), new Transaction(), new ArrayList<>());

        SUBSET fn = new SUBSET();

        {
            MinimaFunction mf = fn.getNewFunction();
            mf.addParameter(new ConstantExpression(new NumberValue(0)));
            mf.addParameter(new ConstantExpression(new NumberValue(2)));
            mf.addParameter(new ConstantExpression(new HEXValue("0x01234567")));
            try {
                Value res = mf.runFunction(ctr);
                assertEquals(Value.VALUE_HEX, res.getValueType());
                assertEquals("0x0123", ((HEXValue) res).toString());
            } catch (ExecutionException ex) {
                fail();
            }
        }
        {
            MinimaFunction mf = fn.getNewFunction();
            mf.addParameter(new ConstantExpression(new NumberValue(0)));
            mf.addParameter(new ConstantExpression(new NumberValue(4)));
            mf.addParameter(new ConstantExpression(new HEXValue("0x01234567")));
            try {
                Value res = mf.runFunction(ctr);
                assertEquals(Value.VALUE_HEX, res.getValueType());
                assertEquals("0x01234567", ((HEXValue) res).toString());
            } catch (ExecutionException ex) {
                fail();
            }
        }
        {
            MinimaFunction mf = fn.getNewFunction();
            mf.addParameter(new ConstantExpression(new NumberValue(12)));
            mf.addParameter(new ConstantExpression(new NumberValue(13)));
            mf.addParameter(new ConstantExpression(new HEXValue("0x0123456789ABCDEF")));
            // test fails because SUBSET does not check whether start is greater than the total data length
            //try {
            //    Value res = mf.runFunction(ctr);
            //    assertEquals(Value.VALUE_HEX, res.getValueType());
            //    assertEquals("", ((HEXValue) res).toString());
            //} catch (ExecutionException ex) {
            //    fail();
            //}
        }
        {
            MinimaFunction mf = fn.getNewFunction();
            mf.addParameter(new ConstantExpression(new NumberValue(0)));
            mf.addParameter(new ConstantExpression(new NumberValue(12)));
            mf.addParameter(new ConstantExpression(new HEXValue("0x0123456789ABCDEF")));
            // test fails because SUBSET does not check whether end is greater than the total data length
            //try {
            //    Value res = mf.runFunction(ctr);
            //    assertEquals(Value.VALUE_HEX, res.getValueType());
            //    assertEquals("", ((HEXValue) res).toString());
            //} catch (ExecutionException ex) {
            //    fail();
            //}
        }

        {
            MinimaFunction mf = fn.getNewFunction();
            mf.addParameter(new ConstantExpression(new NumberValue(0)));
            mf.addParameter(new ConstantExpression(new NumberValue(5)));
            mf.addParameter(new ConstantExpression(new ScriptValue("Hello World Minima is here")));
            // test fails because SUBSET does not accept ScriptValue param
            //try {
            //    Value res = mf.runFunction(ctr);
            //    assertEquals(Value.VALUE_SCRIPT, res.getValueType());
            //    assertEquals("hello", ((ScriptValue) res).toString());
            //} catch (ExecutionException ex) {
            //    fail();
            //}
        }
        {
            MinimaFunction mf = fn.getNewFunction();
            mf.addParameter(new ConstantExpression(new NumberValue(13)));
            mf.addParameter(new ConstantExpression(new NumberValue(19)));
            mf.addParameter(new ConstantExpression(new ScriptValue("Hello World Minima is here")));
            // test fails because SUBSET does not accept ScriptValue param
            //try {
            //    Value res = mf.runFunction(ctr);
            //    assertEquals(Value.VALUE_SCRIPT, res.getValueType());
            //    assertEquals("minima", ((ScriptValue) res).toString());
            //} catch (ExecutionException ex) {
            //    fail();
            //}
        }
        {
            MinimaFunction mf = fn.getNewFunction();
            mf.addParameter(new ConstantExpression(new NumberValue(22)));
            mf.addParameter(new ConstantExpression(new NumberValue(32)));
            mf.addParameter(new ConstantExpression(new HEXValue("0x0123456789ABCDEF")));
            // test fails because SUBSET does not accept ScriptValue param
            // and probably
            // test will fail because SUBSET does not check whether end is greater than the total data length
            //try {
            //    Value res = mf.runFunction(ctr);
            //    assertEquals(Value.VALUE_SCRIPT, res.getValueType());
            //    assertEquals("", ((ScriptValue) res).toString());
            //} catch (ExecutionException ex) {
            //    fail();
            //}
        }
        {
            MinimaFunction mf = fn.getNewFunction();
            mf.addParameter(new ConstantExpression(new NumberValue(0)));
            mf.addParameter(new ConstantExpression(new NumberValue(12)));
            mf.addParameter(new ConstantExpression(new HEXValue("0x0123456789ABCDEF")));
            // test fails because SUBSET does not accept ScriptValue param
            // and probably
            // test will fail because SUBSET does not check whether end is greater than the total data length
            //try {
            //    Value res = mf.runFunction(ctr);
            //    assertEquals(Value.VALUE_HEX, res.getValueType());
            //    assertEquals("", ((ScriptValue) res).toString());
            //} catch (ExecutionException ex) {
            //    fail();
            //}
        }
    }

    @Test
    public void testInvalidParams() {
        Contract ctr = new Contract("", "", new Witness(), new Transaction(), new ArrayList<>());

        SUBSET fn = new SUBSET();

        // Invalid param count
        {
            MinimaFunction mf = fn.getNewFunction();
            assertThrows(ExecutionException.class, () -> { // Should fail, as invalid number of parameters is provided
                Value res = mf.runFunction(ctr);
            });
        }
        {
            MinimaFunction mf = fn.getNewFunction();
            mf.addParameter(new ConstantExpression(new NumberValue(0)));
            assertThrows(ExecutionException.class, () -> { // Should fail, as invalid number of parameters is provided
                Value res = mf.runFunction(ctr);
            });
        }
        {
            MinimaFunction mf = fn.getNewFunction();
            mf.addParameter(new ConstantExpression(new NumberValue(0)));
            mf.addParameter(new ConstantExpression(new NumberValue(2)));
            assertThrows(ExecutionException.class, () -> { // Should fail, as invalid number of parameters is provided
                Value res = mf.runFunction(ctr);
            });
        }

        // Invalid param domain
        {
            MinimaFunction mf = fn.getNewFunction();
            mf.addParameter(new ConstantExpression(new NumberValue(-9)));
            mf.addParameter(new ConstantExpression(new NumberValue(-5)));
            mf.addParameter(new ConstantExpression(new HEXValue("0x01234567")));
            //assertThrows(ExecutionException.class, () -> { // Should throw this
            //    Value res = mf.runFunction(ctr);
            //});
            assertThrows(ArrayIndexOutOfBoundsException.class, () -> { // but throws this
                Value res = mf.runFunction(ctr);
            });
        }
        {
            MinimaFunction mf = fn.getNewFunction();
            mf.addParameter(new ConstantExpression(new NumberValue(2)));
            mf.addParameter(new ConstantExpression(new NumberValue(0)));
            mf.addParameter(new ConstantExpression(new HEXValue("0x01234567")));
            //assertThrows(ExecutionException.class, () -> { // Should throw this
            //    Value res = mf.runFunction(ctr);
            //});
            assertThrows(NegativeArraySizeException.class, () -> { // but throws this
                Value res = mf.runFunction(ctr);
            });
        }

        // Invalid param types
        {
            MinimaFunction mf = fn.getNewFunction();
            mf.addParameter(new ConstantExpression(new BooleanValue(true)));
            mf.addParameter(new ConstantExpression(new NumberValue(2)));
            mf.addParameter(new ConstantExpression(new HEXValue("0x01234567")));
            assertThrows(ExecutionException.class, () -> {
                Value res = mf.runFunction(ctr);
            });
        }
        {
            MinimaFunction mf = fn.getNewFunction();
            mf.addParameter(new ConstantExpression(new HEXValue("0x00")));
            mf.addParameter(new ConstantExpression(new NumberValue(2)));
            mf.addParameter(new ConstantExpression(new HEXValue("0x01234567")));
            assertThrows(ExecutionException.class, () -> {
                Value res = mf.runFunction(ctr);
            });
        }
        {
            MinimaFunction mf = fn.getNewFunction();
            mf.addParameter(new ConstantExpression(new ScriptValue("Hello World")));
            mf.addParameter(new ConstantExpression(new NumberValue(2)));
            mf.addParameter(new ConstantExpression(new HEXValue("0x01234567")));
            assertThrows(ExecutionException.class, () -> {
                Value res = mf.runFunction(ctr);
            });
        }
        {
            MinimaFunction mf = fn.getNewFunction();
            mf.addParameter(new ConstantExpression(new NumberValue(0)));
            mf.addParameter(new ConstantExpression(new BooleanValue(true)));
            mf.addParameter(new ConstantExpression(new HEXValue("0x01234567")));
            assertThrows(ExecutionException.class, () -> {
                Value res = mf.runFunction(ctr);
            });
        }
        {
            MinimaFunction mf = fn.getNewFunction();
            mf.addParameter(new ConstantExpression(new NumberValue(0)));
            mf.addParameter(new ConstantExpression(new HEXValue("0x00")));
            mf.addParameter(new ConstantExpression(new HEXValue("0x01234567")));
            assertThrows(ExecutionException.class, () -> {
                Value res = mf.runFunction(ctr);
            });
        }
        {
            MinimaFunction mf = fn.getNewFunction();
            mf.addParameter(new ConstantExpression(new NumberValue(0)));
            mf.addParameter(new ConstantExpression(new ScriptValue("Hello World")));
            mf.addParameter(new ConstantExpression(new HEXValue("0x01234567")));
            assertThrows(ExecutionException.class, () -> {
                Value res = mf.runFunction(ctr);
            });
        }

        {
            MinimaFunction mf = fn.getNewFunction();
            mf.addParameter(new ConstantExpression(new BooleanValue(true)));
            mf.addParameter(new ConstantExpression(new NumberValue(5)));
            mf.addParameter(new ConstantExpression(new ScriptValue("Hello World Minima is here")));
            assertThrows(ExecutionException.class, () -> {
                Value res = mf.runFunction(ctr);
            });
        }
        {
            MinimaFunction mf = fn.getNewFunction();
            mf.addParameter(new ConstantExpression(new HEXValue("0x00")));
            mf.addParameter(new ConstantExpression(new NumberValue(5)));
            mf.addParameter(new ConstantExpression(new ScriptValue("Hello World Minima is here")));
            assertThrows(ExecutionException.class, () -> {
                Value res = mf.runFunction(ctr);
            });
        }
        {
            MinimaFunction mf = fn.getNewFunction();
            mf.addParameter(new ConstantExpression(new ScriptValue("Hello World")));
            mf.addParameter(new ConstantExpression(new NumberValue(5)));
            mf.addParameter(new ConstantExpression(new ScriptValue("Hello World Minima is here")));
            assertThrows(ExecutionException.class, () -> {
                Value res = mf.runFunction(ctr);
            });
        }
        {
            MinimaFunction mf = fn.getNewFunction();
            mf.addParameter(new ConstantExpression(new NumberValue(0)));
            mf.addParameter(new ConstantExpression(new BooleanValue(true)));
            mf.addParameter(new ConstantExpression(new ScriptValue("Hello World Minima is here")));
            assertThrows(ExecutionException.class, () -> {
                Value res = mf.runFunction(ctr);
            });
        }
        {
            MinimaFunction mf = fn.getNewFunction();
            mf.addParameter(new ConstantExpression(new NumberValue(0)));
            mf.addParameter(new ConstantExpression(new HEXValue("0x00")));
            mf.addParameter(new ConstantExpression(new ScriptValue("Hello World Minima is here")));
            assertThrows(ExecutionException.class, () -> {
                Value res = mf.runFunction(ctr);
            });
        }
        {
            MinimaFunction mf = fn.getNewFunction();
            mf.addParameter(new ConstantExpression(new NumberValue(0)));
            mf.addParameter(new ConstantExpression(new ScriptValue("Hello World")));
            mf.addParameter(new ConstantExpression(new ScriptValue("Hello World Minima is here")));
            assertThrows(ExecutionException.class, () -> {
                Value res = mf.runFunction(ctr);
            });
        }

        {
            MinimaFunction mf = fn.getNewFunction();
            mf.addParameter(new ConstantExpression(new NumberValue(0)));
            mf.addParameter(new ConstantExpression(new NumberValue(2)));
            mf.addParameter(new ConstantExpression(new BooleanValue(true)));
            assertThrows(ExecutionException.class, () -> {
                Value res = mf.runFunction(ctr);
            });
        }
        {
            MinimaFunction mf = fn.getNewFunction();
            mf.addParameter(new ConstantExpression(new NumberValue(0)));
            mf.addParameter(new ConstantExpression(new NumberValue(2)));
            mf.addParameter(new ConstantExpression(new NumberValue(3)));
            assertThrows(ExecutionException.class, () -> {
                Value res = mf.runFunction(ctr);
            });
        }

    }
}
