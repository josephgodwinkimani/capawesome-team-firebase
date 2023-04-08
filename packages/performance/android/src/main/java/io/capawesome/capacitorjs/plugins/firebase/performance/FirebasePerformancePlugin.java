package io.capawesome.capacitorjs.plugins.firebase.performance;

import com.getcapacitor.JSObject;
import com.getcapacitor.Logger;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.google.firebase.perf.metrics.Trace;

@CapacitorPlugin(name = "FirebasePerformance")
public class FirebasePerformancePlugin extends Plugin {

    public static final String TAG = "FirebasePerformance";
    public static final String ERROR_TRACE_NAME_MISSING = "traceName must be provided.";
    public static final String ERROR_METRIC_NAME_MISSING = "metricName must be provided.";
    public static final String ERROR_TRACE_NAME_ALREADY_ASSIGNED = "traceName already assigned.";
    public static final String ERROR_ENABLED_MISSING = "enabled must be provided.";
    public static final String ERROR_TRACE_NOT_FOUND = "No trace was found with the provided traceName.";
    private FirebasePerformance implementation = new FirebasePerformance();

    @PluginMethod
    public void startTrace(PluginCall call) {
        try {
            String traceName = call.getString("traceName");
            if (traceName == null) {
                call.reject(ERROR_TRACE_NAME_MISSING);
                return;
            }
            Trace trace = implementation.getTraceByName(traceName);
            if (trace != null) {
                call.reject(ERROR_TRACE_NAME_ALREADY_ASSIGNED);
                return;
            }
            implementation.startTrace(traceName);
            call.resolve();
        } catch (Exception exception) {
            Logger.error(TAG, exception.getMessage(), exception);
            call.reject(exception.getMessage());
        }
    }

    @PluginMethod
    public void stopTrace(PluginCall call) {
        try {
            String traceName = call.getString("traceName");
            if (traceName == null) {
                call.reject(ERROR_TRACE_NAME_MISSING);
                return;
            }
            Trace trace = implementation.getTraceByName(traceName);
            if (trace == null) {
                call.reject(ERROR_TRACE_NOT_FOUND);
                return;
            }
            implementation.stopTrace(traceName);
            call.resolve();
        } catch (Exception exception) {
            Logger.error(TAG, exception.getMessage(), exception);
            call.reject(exception.getMessage());
        }
    }

    @PluginMethod
    public void incrementMetric(PluginCall call) {
        try {
            String traceName = call.getString("traceName");
            String metricName = call.getString("metricName");
            Integer incrementBy = call.getInt("incrementBy", 1);
            if (traceName == null) {
                call.reject(ERROR_TRACE_NAME_MISSING);
                return;
            }
            if (metricName == null) {
                call.reject(ERROR_METRIC_NAME_MISSING);
                return;
            }
            Trace trace = implementation.getTraceByName(traceName);
            if (trace == null) {
                call.reject(ERROR_TRACE_NOT_FOUND);
                return;
            }
            implementation.incrementMetric(traceName, metricName, incrementBy);
            call.resolve();
        } catch (Exception exception) {
            Logger.error(TAG, exception.getMessage(), exception);
            call.reject(exception.getMessage());
        }
    }

    @PluginMethod
    public void setEnabled(PluginCall call) {
        try {
            Boolean enabled = call.getBoolean("enabled");
            if (enabled == null) {
                call.reject(ERROR_ENABLED_MISSING);
                return;
            }
            implementation.setEnabled(enabled);
            call.resolve();
        } catch (Exception exception) {
            Logger.error(TAG, exception.getMessage(), exception);
            call.reject(exception.getMessage());
        }
    }

    @PluginMethod
    public void isEnabled(PluginCall call) {
        try {
            Boolean enabled = implementation.isEnabled();
            JSObject result = new JSObject();
            result.put("enabled", enabled);
            call.resolve(result);
        } catch (Exception exception) {
            Logger.error(TAG, exception.getMessage(), exception);
            call.reject(exception.getMessage());
        }
    }
}
