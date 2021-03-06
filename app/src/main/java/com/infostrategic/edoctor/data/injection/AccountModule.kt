package com.infostrategic.edoctor.data.injection

import com.infostrategic.edoctor.data.remote.rest.AccountRestApi
import com.infostrategic.edoctor.data.repository.AccountRepository
import com.infostrategic.edoctor.data.session.SessionManager
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Named

@Module
class AccountModule {

    @Provides
    internal fun provideAccountRestApi(
        @Named(NetworkModule.AUTHORIZED_TAG)
        builder: Retrofit.Builder
    ): AccountRestApi = builder.build().create(AccountRestApi::class.java)

    @Provides
    internal fun provideAccountRepository(
        api: AccountRestApi,
        sessionManager: SessionManager
    ): AccountRepository = AccountRepository(api, sessionManager)

}