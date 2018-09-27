package com.tutao.coolweather.api;

import com.tutao.common.dto.MaterialDto;

import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by jingting on 2018/1/25.
 */

public interface MaterialLibraryApi {
    @FormUrlEncoded
    @POST()
    Observable<MaterialDto> getMaterialLibrary();
}
