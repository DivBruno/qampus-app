import { Injectable } from '@angular/core';

export interface Tag {
  id: string;
  name: string;
}

export interface Post {
  id: string;
  title: string;
  content: string;
  upVotes: number;
  downVotes: number;
  tags: Tag[];
  createdAt: string;
}

export interface NewPost{
  title: string,
  content: string,
  tags: string[]
}

@Injectable({
  providedIn: 'root'
})
export class PostService {

  private apiUrl = 'http://localhost:8080/post';

  async findAll(): Promise<Post[]> {
    const response = await fetch(this.apiUrl, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer '+localStorage.getItem('token')
      }
    });

    if (!response.ok) {
      throw new Error('Erro ao buscar dúvidas');
    }

    return await response.json();
  }

  async createPost(post: NewPost){
    try{
      const response = await fetch(this.apiUrl+"/create", {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer '+localStorage.getItem('token')
        },
        body: JSON.stringify(post)
      })
      return response.ok;
    }catch(error){
      console.error("Error creating new Post: ", error);
      return false;
    }
  }
  async findById(id: string): Promise<Post> {
    const response = await fetch(`${this.apiUrl}/${id}`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer '+localStorage.getItem('token')
      }
    });

    if (!response.ok) {
      throw new Error('Erro ao buscar dúvida');
    }

    return await response.json();
  }
}