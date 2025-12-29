package com.cosmic_struck.stellar.classroom.presentation.viewmodel

import com.cosmic_struck.stellar.classroom.data.dto.ClassroomMember
import com.cosmic_struck.stellar.classroom.data.dto.ClassroomModel


data class ClassroomHomeScreenState(
    val classroom_id : String = "",
    val classroomName: String = "",
    val classroomAuthor : String = "",
    val classroomMembers: String = "",
    val classroomMembersList: List<ClassroomMember> = emptyList(),
    val classroomModelsList: List<ClassroomModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val options: List<Options> = listOf(Options.MEMBERS,Options.MODELS),
    val selected : Options = Options.MEMBERS
)

enum class Options{
    MEMBERS,
    MODELS
}