import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CreatePost } from './create-post';
import { PostService, NewPost } from '../post-service';
import { Router } from '@angular/router';

describe('CreatePost', () => {
  let component: CreatePost;
  let fixture: ComponentFixture<CreatePost>;

  let routerMock: {
    navigate: ReturnType<typeof vi.fn>;
  }

  beforeEach(async () => {
    routerMock={
      navigate: vi.fn(),
    }
    await TestBed.configureTestingModule({
      imports: [CreatePost],
      providers: [
        PostService,
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

  it('should create a new post through DuvidaService an', async() => {
    const fetchMock = vi.spyOn(globalThis, 'fetch').mockResolvedValue({
        ok: true
    } as Response);

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

    expect(fetchMock).toHaveBeenCalledWith(
        expect.stringContaining('/create'),
        expect.objectContaining({
            method: 'POST',
        })
    );

    expect(routerMock.navigate).toHaveBeenCalledWith(['home']);
  });

  it('should show an alert when creating post fails', async()=>{
    const fetchMock = vi.spyOn(globalThis, 'fetch').mockResolvedValue({
        ok: false
    } as Response);

    const alertMock = vi
      .spyOn(window, 'alert')
      .mockImplementation(()=>{});
    
    component.postForm.setValue({
      title: 'TESTE',
      content: 'TESTES'
    })
    await component.submit()
    const post: NewPost = {
      title: 'TESTE',
      content: 'TESTES',
      tags: []
    }
    expect(routerMock.navigate).not.toHaveBeenCalledWith(['home']);
    expect(alertMock).toHaveBeenCalledWith("Erro ao publicar uma nova dúvida");
    alertMock.mockRestore();
  })
});
