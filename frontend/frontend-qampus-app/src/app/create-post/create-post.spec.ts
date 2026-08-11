import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreatePost } from './create-post';
import { DuvidaService } from '../duvida-service';
import { Router } from '@angular/router';
import { Duvida } from '../duvida';

describe('CreatePost', () => {
  let component: CreatePost;
  let fixture: ComponentFixture<CreatePost>;

  let duvidaService: {
    createDuvida: ReturnType<typeof vi.fn>;
  }
  let routerMock: {
    navigate: ReturnType<typeof vi.fn>;
  }

  beforeEach(async () => {
    duvidaService = {
      createDuvida: vi.fn(),
    }
    routerMock={
      navigate: vi.fn(),
    }
    await TestBed.configureTestingModule({
      imports: [CreatePost],
      providers: [
        {
          provide: DuvidaService,
          useValue: duvidaService
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
    duvidaService.createDuvida.mockResolvedValue(true);
    component.postForm.setValue({
      title: 'TESTE',
      content: 'TESTES'
    })
    await component.submit();
    const duvida: Duvida = {
      title: 'TESTE',
      content: 'TESTES',
      tags: []
    }
    expect(duvidaService.createDuvida).toHaveBeenCalledWith(duvida);
  });

  it('should show an alert when creating post fails', async()=>{
    duvidaService.createDuvida.mockResolvedValue(false);
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
