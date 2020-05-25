package com.educatorapp.utils.states

sealed class CommentState {
    object EditComment : CommentState()
    object DeleteComment : CommentState()
}