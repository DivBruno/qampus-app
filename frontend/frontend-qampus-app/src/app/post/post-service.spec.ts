import { TestBed } from '@angular/core/testing';
import { NewPost, PostService } from './post-service';

describe('PostService', () => {
  let service: PostService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PostService);
  });

  it('should create new post succesfully', async() => {
    const post: NewPost = {
      title: 'TESTE',
      content: 'TESTES',
      tags: []
    }
    const token = 'token-teste';
    vi.spyOn(globalThis, 'fetch').mockResolvedValue(
      new Response(
        JSON.stringify(post),
        {
          status: 200,
          headers: {'Content-Type': 'application/json',
            'Authorization': 'Bearer '+token
          }
        }
      )
    )
    const result = await service.createPost(post);
    expect(result).toBe(true);
  });

  it('should return false when creating a new post fails', async()=>{
    vi.spyOn(globalThis, 'fetch').mockResolvedValue(
      new Response(null, {status: 401})
    );
    const post: NewPost = {
      title: 'TESTE',
      content: 'TESTES',
      tags: []
    }

    const result = await service.createPost(post);
    expect(result).toBe(false);
  })

  it('should return false when creating a new post throws an error', async()=>{
    vi.spyOn(globalThis, 'fetch').mockRejectedValue(
      new Error('Erro de conexão')
    );
    const post: NewPost = {
      title: 'TESTE',
      content: 'TESTES',
      tags: []
    }
    const result = await service.createPost(post);
    expect(result).toBe(false);
  })
});
