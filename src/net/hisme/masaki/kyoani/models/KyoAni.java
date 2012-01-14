package net.hisme.masaki.kyoani.models;

import android.content.Context;

public class KyoAni {
  private Context context;

  private Class<ScheduleService> schedule_service = null;

  /**
   * create KyoAni instance with Context
   * 
   * @param context
   */
  public KyoAni(Context context) {
    this.context = context;
  }

  /**
   * get context
   * 
   * @return context
   */
  public Context context() {
    return this.context;
  }

  public void setScheduleService(Class<ScheduleService> service_class) {
    this.schedule_service = service_class;
  }

  public Class<ScheduleService> getScheduleService() {
    return this.schedule_service;
  }
}
