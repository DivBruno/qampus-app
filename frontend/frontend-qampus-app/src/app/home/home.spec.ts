import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Home } from './home';
import { Router } from '@angular/router';
import { vi } from 'vitest';

describe('Home', () => {
  let component: Home;
  let fixture: ComponentFixture<Home>;

  let routerMock: {
    navigate: ReturnType<typeof vi.fn>;
  };

  beforeEach(async () => {
    routerMock = {
      navigate: vi.fn(),
    };

    await TestBed.configureTestingModule({
      imports: [Home],
      providers: [
        {
          provide: Router,
          useValue: routerMock,
        },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(Home);
    component = fixture.componentInstance;

    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should contain the mocked doubts', () => {
    expect(component.duvidas.length).toBe(3);

    expect(component.duvidas[0].titulo).toBe(
      'Como funciona a matrícula nas disciplinas?'
    );

    expect(component.duvidas[0].votos).toBe(12);
    expect(component.duvidas[0].respostas).toBe(4);
  });

  it('should navigate to create question page', () => {
    component.fazerPergunta();

    expect(routerMock.navigate).toHaveBeenCalledWith(['/duvida/criar']);
  });

  it('should navigate to the selected question', () => {
    component.visualizarDuvida(2);

    expect(routerMock.navigate).toHaveBeenCalledWith(['/duvida', 2]);
  });
});