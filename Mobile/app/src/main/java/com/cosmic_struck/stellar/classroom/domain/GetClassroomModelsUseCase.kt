package com.cosmic_struck.stellar.classroom.domain

import com.cosmic_struck.stellar.classroom.data.dto.ClassroomModel
import com.cosmic_struck.stellar.common.util.Resource
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import io.github.jan.supabase.postgrest.rpc
import javax.inject.Inject

class GetClassroomModelsUseCase @Inject constructor(private val client: SupabaseClient) {
    operator fun invoke(classId: String): Flow<Resource<List<ClassroomModel>>> = flow {
        emit(Resource.Loading())
        try {
            val response = client.postgrest.rpc(
                function = "get_classroom_models",
                parameters = mapOf("p_classroom_id" to classId)
            ).decodeList<ClassroomModel>()

            emit(Resource.Success(response))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Failed to load models for this classroom"))
        }
    }
}