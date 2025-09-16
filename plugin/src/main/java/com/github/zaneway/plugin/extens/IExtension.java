package com.github.zaneway.plugin.extens;

import com.github.zaneway.plugin.entity.BaseRequest;
import com.github.zaneway.plugin.entity.BaseResponse;
import org.pf4j.ExtensionPoint;

public interface IExtension extends ExtensionPoint {

  BaseResponse execute(BaseRequest request);


}
