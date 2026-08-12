import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute, Router } from '@angular/router';
import { vi } from 'vitest';

import { Duvida } from './duvida';
import { Post, PostService } from '../post/post-service';

describe('Duvida', () => {
  let component: Duvida;
  let fixture: ComponentFixture<Duvida>;

  let routerMock: {
    navigate: ReturnType<typeof vi.fn>;
  };

  const postMock: Post = {
    id: '1',
    title: 'Como funciona a matrícula nas disciplinas?',
    content: 'Conteúdo da dúvida',
    upVotes: 12,
    downVotes: 3,
    tags: [
      {
        id: '1',
        name: 'CURSO'
      },
      {
        id: '2',
        name: 'TURMA 1'
      }
    ],
    createdAt: '2026-08-11T10:00:00'
  };

  const postServiceMock = {
    findById: vi.fn()
  };

  beforeEach(async () => {
    routerMock = {
      navigate: vi.fn(),
    };

    postServiceMock.findById.mockResolvedValue({
      ...postMock,
      tags: [...postMock.tags],
    });

    await TestBed.configureTestingModule({
      imports: [Duvida],
      providers: [
        {
          provide: Router,
          useValue: routerMock,
        },
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: {
                get: vi.fn().mockReturnValue('1'),
              },
            },
          },
        },
        {
          provide: PostService,
          useValue: postServiceMock,
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

  it('should load the post by id', () => {
    expect(postServiceMock.findById).toHaveBeenCalledWith('1');

    expect(component.post).toBeTruthy();

    expect(component.post?.id).toBe('1');
    expect(component.post?.title).toBe(
      'Como funciona a matrícula nas disciplinas?'
    );
    expect(component.post?.content).toBe('Conteúdo da dúvida');
  });

  it('should contain the post votes', () => {
    expect(component.post?.upVotes).toBe(12);
    expect(component.post?.downVotes).toBe(3);
  });

  it('should contain the post tags', () => {
    expect(component.post?.tags.length).toBe(2);
    expect(component.post?.tags[0].name).toBe('CURSO');
    expect(component.post?.tags[1].name).toBe('TURMA 1');
  });

  it('should increase the up votes', () => {
    component.votar(1);

    expect(component.post?.upVotes).toBe(13);
    expect(component.post?.downVotes).toBe(3);
  });

  it('should increase the down votes', () => {
    component.votar(-1);

    expect(component.post?.upVotes).toBe(12);
    expect(component.post?.downVotes).toBe(4);
  });

  it('should navigate to the selected related question', () => {
    component.visualizarRelacionada('3');

    expect(routerMock.navigate).toHaveBeenCalledWith([
      '/duvida',
      '3'
    ]);
  });
});