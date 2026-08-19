import { Component} from '@angular/core';
import { Router } from '@angular/router';
import { FormControl, FormGroup, FormsModule, Validators, ReactiveFormsModule } from '@angular/forms';
import { Navbar } from "../../navbar/navbar";
import { PostService, NewPost} from '../post-service';

@Component({
  selector: 'app-create-post',
  imports: [FormsModule, ReactiveFormsModule, Navbar],
  templateUrl: './create-post.html',
  styleUrl: './create-post.css',
})

export class CreatePost {
  constructor(
    private postService: PostService,
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

  abrirFecharTag() {
    this.novaTag = !this.novaTag;
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

  removeTag(tag: string){
    const tagRemovida = this.tagsCriadas.indexOf(tag);
    this.tagsCriadas.splice(tagRemovida, 1);
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
    }else{
      alert("Todos os dados devem estar preenchidos");
    }
  }
}
