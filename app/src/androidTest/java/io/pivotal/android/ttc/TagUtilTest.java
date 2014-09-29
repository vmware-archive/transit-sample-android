package io.pivotal.android.ttc;

import android.test.AndroidTestCase;

import java.util.TimeZone;

import io.pivotal.android.ttc.route.Route;
import io.pivotal.android.ttc.stop.Stop;
import io.pivotal.android.ttc.util.TagUtil;

public class TagUtilTest extends AndroidTestCase {

    private static final Route TEST_ROUTE = new Route();
    private static final Stop TEST_STOP = new Stop();

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        TEST_ROUTE.tag = "ROUTE";
        TEST_ROUTE.title = "XXXX";
        TEST_STOP.stopId = "STOP";
        TEST_STOP.tag = "XXXX";
        TEST_STOP.title = "XXXX";

        // Hopefully this works with Daylight Savings time
        final TimeZone timeZone = TimeZone.getTimeZone("GMT-04:00");
        TagUtil.setLocalTimeZone(timeZone);
    }

    public void testNullInputs() {
        assertNull(TagUtil.getTag(0, 0, null, null));
        assertNull(TagUtil.getTag(0, 0, TEST_ROUTE, null));
        assertNull(TagUtil.getTag(0, 0, null, TEST_STOP));
    }

    public void testValidInputs() {
        assertEquals("0400_ROUTE_STOP", TagUtil.getTag( 0,  0, TEST_ROUTE, TEST_STOP));
        assertEquals("0401_ROUTE_STOP", TagUtil.getTag( 0,  1, TEST_ROUTE, TEST_STOP));
        assertEquals("0500_ROUTE_STOP", TagUtil.getTag( 1,  0, TEST_ROUTE, TEST_STOP));
        assertEquals("0501_ROUTE_STOP", TagUtil.getTag( 1,  1, TEST_ROUTE, TEST_STOP));
        assertEquals("1309_ROUTE_STOP", TagUtil.getTag( 9,  9, TEST_ROUTE, TEST_STOP));
        assertEquals("1410_ROUTE_STOP", TagUtil.getTag(10, 10, TEST_ROUTE, TEST_STOP));
        assertEquals("2359_ROUTE_STOP", TagUtil.getTag(19, 59, TEST_ROUTE, TEST_STOP));
        assertEquals("0000_ROUTE_STOP", TagUtil.getTag(20,  0, TEST_ROUTE, TEST_STOP));
        assertEquals("0001_ROUTE_STOP", TagUtil.getTag(20,  1, TEST_ROUTE, TEST_STOP));
    }
}
