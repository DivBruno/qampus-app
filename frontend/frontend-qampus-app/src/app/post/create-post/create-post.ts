import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormControl, FormGroup, FormsModule, Validators, ReactiveFormsModule } from '@angular/forms';
import { AuthService } from '../../auth/auth-service';
import { Navbar } from "../../navbar/navbar";
import { PostService, NewPost } from '../post-service';

@Component({
  selector: 'app-create-post',
  imports: [FormsModule, ReactiveFormsModule, Navbar],
  templateUrl: './create-post.html',
  styleUrl: './create-post.css',
})

export class CreatePost {
  constructor(
    private postService: PostService,
    private authService: AuthService,
    private router: Router
  ){}

  novaTag = false;
  nomeTag = '';
  quantidadeTags = 0;
  tagsCriadas: string[] = [];

  postForm = new FormGroup({
    title: new FormControl('', Validators.required),
    content: new FormControl('', Validators.required)
  })

  abrirNovaTag() {
    this.novaTag = true;
  }

  fecharNovaTag() {
    this.novaTag = false;
  }

  adicionarTag() {
      if (this.nomeTag.trim() !== '') {
          this.tagsCriadas.push(this.nomeTag.trim());
          this.nomeTag = '';
          this.novaTag = false;
      }
  }

  goTo(rota: string){
    this.router.navigate([rota]);
  }

  async submit(){
    if(this.postForm.valid){
      const post: NewPost = {
        title: this.postForm.value.title!,
        content: this.postForm.value.content!,
        tags: this.tagsCriadas
      }
      const response = await this.postService.createPost(post);
      if(response){
        this.router.navigate(['home']);
      }else{
        alert("Erro ao publicar uma nova dúvida");
      }
    }
  }
}
