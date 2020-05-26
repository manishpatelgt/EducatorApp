package com.educatorapp.utils.states

/**
 * Created by Manish Patel on 5/26/2020.
 */
sealed class FavState {
    object FavRemove: FavState()
    object FavClick: FavState()
}