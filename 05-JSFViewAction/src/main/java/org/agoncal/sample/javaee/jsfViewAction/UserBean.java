package org.agoncal.sample.javaee.jsfViewAction;

import java.io.Serializable;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named
@ViewScoped
public class UserBean implements Serializable {

    private String page;

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }
    
    public Object paginate() {
        String page = (null != getPage()) ? getPage() : "first";
        if ("2".equals(page)) {
            page = "second";
        } 
        return page;
    }
}

