import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Navbar } from '../../navbar/navbar';
import { Post, PostService } from '../post-service';
import { ChangeDetectorRef } from '@angular/core';

@Component({
  selector: 'app-duvida',
  imports: [Navbar],
  templateUrl: './duvida.html',
  styleUrl: './duvida.css',
})
export class Duvida implements OnInit {

  post: Post | null = null;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private postService: PostService,
    private cdr: ChangeDetectorRef
  ) {}

  async ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    if (!id) {
      return;
    }

    try {
      this.post = await this.postService.findById(id);
      this.cdr.detectChanges();
    } catch (error) {
      console.error('Erro ao carregar dúvida:', error);
    }
  }

  votar(valor: number): void {
    if (!this.post) {
      return;
    }

    if (valor > 0) {
      this.post.upVotes++;
    } else {
      this.post.downVotes++;
    }
  }

  editarDuvida(){
    this.router.navigate(['/post/editar', this.post?.id]);
  }
  visualizarRelacionada(id: string): void {
    this.router.navigate(['/duvida', id]);
  }
}