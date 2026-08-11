import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Duvida } from './duvida';
import { AuthService } from '../auth/auth-service';
import { Router } from '@angular/router';
import { vi } from 'vitest';

describe('Duvida', () => {
  let component: Duvida;
  let fixture: ComponentFixture<Duvida>;

  let authServiceMock: {
    logout: ReturnType<typeof vi.fn>;
  };

  let routerMock: {
    navigate: ReturnType<typeof vi.fn>;
  };

  beforeEach(async () => {
    authServiceMock = {
      logout: vi.fn(),
    };

    routerMock = {
      navigate: vi.fn(),
    };

    await TestBed.configureTestingModule({
      imports: [Duvida],
      providers: [
        {
          provide: AuthService,
          useValue: authServiceMock,
        },
        {
          provide: Router,
          useValue: routerMock,
        },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(Duvida);
    component = fixture.componentInstance;

    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have the initial question data', () => {
    expect(component.titulo).toBe('Dúvida 1');
    expect(component.votos).toBe(0);
    expect(component.tags.length).toBe(2);
  });

  it('should contain the expected responses', () => {
    expect(component.respostas.length).toBe(3);
    expect(component.respostas[0].id).toBe(1);
    expect(component.respostas[1].id).toBe(2);
    expect(component.respostas[2].id).toBe(3);
  });

  it('should contain the related questions', () => {
    expect(component.relacionadas.length).toBe(4);
    expect(component.relacionadas[0].id).toBe(2);
    expect(component.relacionadas[0].votos).toBe(4);
  });

  it('should increase votes', () => {
    component.votar(1);

    expect(component.votos).toBe(1);
  });

  it('should decrease votes', () => {
    component.votar(-1);

    expect(component.votos).toBe(-1);
  });

  it('should navigate to the selected related question', () => {
    component.visualizarRelacionada(3);

    expect(routerMock.navigate).toHaveBeenCalledWith(['/duvida', 3]);
  });
});