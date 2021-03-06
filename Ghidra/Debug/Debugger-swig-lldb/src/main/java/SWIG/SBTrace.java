/* ###
 * IP: Apache License 2.0 with LLVM Exceptions
 */
/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.1
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package SWIG;

public class SBTrace {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected SBTrace(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(SBTrace obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  @SuppressWarnings("deprecation")
  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        lldbJNI.delete_SBTrace(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public SBTrace() {
    this(lldbJNI.new_SBTrace(), true);
  }

  public String GetStartConfigurationHelp() {
    return lldbJNI.SBTrace_GetStartConfigurationHelp(swigCPtr, this);
  }

  public SBError Start(SBStructuredData configuration) {
    return new SBError(lldbJNI.SBTrace_Start__SWIG_0(swigCPtr, this, SBStructuredData.getCPtr(configuration), configuration), true);
  }

  public SBError Start(SBThread thread, SBStructuredData configuration) {
    return new SBError(lldbJNI.SBTrace_Start__SWIG_1(swigCPtr, this, SBThread.getCPtr(thread), thread, SBStructuredData.getCPtr(configuration), configuration), true);
  }

  public SBError Stop() {
    return new SBError(lldbJNI.SBTrace_Stop__SWIG_0(swigCPtr, this), true);
  }

  public SBError Stop(SBThread thread) {
    return new SBError(lldbJNI.SBTrace_Stop__SWIG_1(swigCPtr, this, SBThread.getCPtr(thread), thread), true);
  }

  public boolean IsValid() {
    return lldbJNI.SBTrace_IsValid(swigCPtr, this);
  }

}
