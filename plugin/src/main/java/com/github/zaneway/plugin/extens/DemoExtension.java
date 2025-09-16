package com.github.zaneway.plugin.extens;

import com.github.zaneway.plugin.entity.BaseRequest;
import com.github.zaneway.plugin.entity.BaseResponse;
import org.pf4j.Extension;

@Extension
public class DemoExtension implements IExtension{

  @Override
  public BaseResponse execute(BaseRequest request) {

    System.out.println("this is demo extension");
    return null;
  }
}
