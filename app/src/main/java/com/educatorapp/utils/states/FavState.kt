package com.educatorapp.utils.states

sealed class FavState {
    object FavRemove: FavState()
    object FavClick: FavState()
}