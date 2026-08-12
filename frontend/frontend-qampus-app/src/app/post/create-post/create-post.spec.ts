import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PostService, NewPost } from '../post-service';
import { CreatePost } from './create-post';
import { Router } from '@angular/router';

describe('CreatePost', () => {
  let component: CreatePost;
  let fixture: ComponentFixture<CreatePost>;

  let postService: {
    createPost: ReturnType<typeof vi.fn>;
  }
  let routerMock: {
    navigate: ReturnType<typeof vi.fn>;
  }

  beforeEach(async () => {
    postService = {
      createPost: vi.fn(),
    }
    routerMock={
      navigate: vi.fn(),
    }
    await TestBed.configureTestingModule({
      imports: [CreatePost],
      providers: [
        {
          provide: PostService,
          useValue: postService
        },
        {
          provide: Router,
          useValue: routerMock
        }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(CreatePost);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should call DuvidaService createDuvida with a new post', async() => {
    postService.createPost.mockResolvedValue(true);
    component.postForm.setValue({
      title: 'TESTE',
      content: 'TESTES'
    })
    await component.submit();
    const post: NewPost = {
      title: 'TESTE',
      content: 'TESTES',
      tags: []
    }
    expect(postService.createPost).toHaveBeenCalledWith(post);
  });

  it('should show an alert when creating post fails', async()=>{
    postService.createPost.mockResolvedValue(false);
    const alertMock = vi
      .spyOn(window, 'alert')
      .mockImplementation(()=>{});
    component.postForm.setValue({
      title: 'TESTE',
      content: 'TESTES'
    })
    await component.submit()
    expect(alertMock).toHaveBeenCalledWith("Erro ao publicar uma nova dúvida");
    alertMock.mockRestore();
  })

  it('should navigate to the route', ()=>{
    component.goTo("login");
    expect(routerMock.navigate).toHaveBeenCalledWith(['login']);
  })
});
