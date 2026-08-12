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

@Injectable({
  providedIn: 'root'
})
export class PostService {

  private apiUrl = 'http://localhost:8080/post';

  async findAll(): Promise<Post[]> {
    const response = await fetch(this.apiUrl, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json'
      }
    });

    if (!response.ok) {
      throw new Error('Erro ao buscar dúvidas');
    }

    return await response.json();
  }
}