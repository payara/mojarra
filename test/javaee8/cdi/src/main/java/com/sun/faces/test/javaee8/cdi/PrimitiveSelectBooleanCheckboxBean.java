package com.sun.faces.test.javaee8.cdi;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 * @author Azizjon Achilov
 * Created: 25.09.2019
 */
@Named
@RequestScoped
public class PrimitiveSelectBooleanCheckboxBean {

  private boolean checked1;

  private Boolean checked2;


  public boolean isChecked1() {
    return checked1;
  }


  public void setChecked1(boolean checked1) {
    this.checked1 = checked1;
  }


  public Boolean getChecked2() {
    return checked2;
  }


  public void setChecked2(Boolean checked2) {
    this.checked2 = checked2;
  }

}
