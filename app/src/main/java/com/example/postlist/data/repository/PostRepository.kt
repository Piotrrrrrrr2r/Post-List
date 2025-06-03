package com.example.postlist.data.repository

import com.example.postlist.data.api.JsonPlaceholderApi
import com.example.postlist.data.model.Post
import com.example.postlist.data.model.PostWithUser
import com.example.postlist.data.model.ToDo
import com.example.postlist.data.model.User

interface PostRepository {
    suspend fun getPosts(): List<Post>
    suspend fun getPost(postId: Int): Post
    suspend fun getUsers(): List<User>
    suspend fun getUser(userId: Int): User
    suspend fun getUserTodos(userId: Int): List<ToDo>
    suspend fun getPostsWithUsers(): List<PostWithUser>
    suspend fun getPostsByUser(userId: Int): List<Post>
}

class PostRepositoryImpl(private val api: JsonPlaceholderApi) : PostRepository {
    override suspend fun getPosts() = api.getPosts()
    override suspend fun getPost(postId: Int) = api.getPost(postId)
    override suspend fun getUsers() = api.getUsers()
    override suspend fun getUser(userId: Int) = api.getUser(userId)
    override suspend fun getUserTodos(userId: Int) = api.getUserTodos(userId)

    override suspend fun getPostsByUser(userId: Int): List<Post> {
        return api.getPostsByUser(userId)
    }

    override suspend fun getPostsWithUsers(): List<PostWithUser> {
        val posts = getPosts()
        val users = getUsers()
        val userMap = users.associateBy { it.id }
        return posts.map { post ->
            PostWithUser(post, userMap[post.userId])
        }
    }
}